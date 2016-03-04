var game = new Phaser.Game(800, 600, Phaser.AUTO, 'game');

var Duckhunt = function () {
    this.music = null;
};

Duckhunt.PreLoad = function () {
};
Duckhunt.PreLoad.prototype = {

    init: function () {
        this.scale.pageAlignHorizontally = true;
        this.input.keyboard.onUpCallback = function (e) {
            if (e.keyCode === Phaser.Keyboard.ESC) {
                this.state.start('Duckhunt.MainMenu');
            }
        }.bind(this);
    },

    preload: function () {
        this.load.path = 'assets/';
        this.load.image('background', 'images/background.jpg');
        this.load.image('menu', 'images/menu.jpg');
        this.load.image('grass', 'images/grass.png');
        this.load.image('duck', 'images/duck.png');
        this.load.image('sight', 'images/sight.png');
        this.load.audio('background', 'sounds/background.mp3');
        this.load.audio('shoot', 'sounds/shoot.mp3');
    },

    create: function () {
        this.state.start('Duckhunt.MainMenu');
        this.music = this.sound.play('background', 1.0, true);
        //this.scale.scaleMode = Phaser.ScaleManager.SHOW_ALL;
        //this.scale.pageAlignVertically = true;
    }
};

Duckhunt.MainMenu = function () {
};

Duckhunt.MainMenu.prototype = {
    create: function () {
        var background = this.add.image(0, 0, 'menu');
        background.width = this.world.width;
        background.height = this.world.height;

        var grass = this.add.image(0,
            this.world.height - this.cache.getImage('grass').height,
            'grass');
        grass.width = this.world.width;

        var textX = this.world.width / 2 - 100;
        var addText = function (text, heightPct, style) {
            this.add.text(textX, this.world.height * heightPct, text, style);
        }.bind(this);
        var style = {font: '16px Arial'};
        addText('Shoot with left click', 0.65, style);
        addText('Start with left click', 0.68, style);
        addText('Headshots are worth extra points', 0.71, style);
        addText('Play until 15 ducks escape', 0.74, style);
        this.input.onDown.addOnce(this.start, this);

        this.crosshair = new Duckhunt.Crosshair(this, 'sight');
    },
    start: function () {
        this.sound.play('shoot');
        this.state.start('Duckhunt.Game');
    },
    update: function () {
        this.crosshair.update(this.input.mousePointer);
    }
};

Duckhunt.Game = function () {
};

Duckhunt.Game.prototype = {
    create: function () {
        var background = this.add.image(0, 0, 'background');
        background.width = this.world.width;
        background.height = this.world.height;
        var grass = this.add.image(0,
            this.world.height - this.cache.getImage('grass').height,
            'grass');
        grass.width = this.world.width;

        this.duckSystem = new Duckhunt.DuckSystem(this, {
            width: this.world.width,
            height: this.world.height,
            timeBetweenDucks: 333,
            duckImageKey: 'duck'
        });

        this.duckSystem.start();

        this.crosshair = new Duckhunt.Crosshair(this, 'sight');
    },

    update: function () {
        this.crosshair.update(this.input.mousePointer);
        this.duckSystem.update();
    }
};

Duckhunt.Crosshair = function (game, imageKey) {
    this.crosshair = game.add.sprite(game.world.width / 2, game.world.height / 2, imageKey);
    this.crosshair.anchor.setTo(0.5, 0.5);
};
Duckhunt.Crosshair.prototype = {
    update: function (mouseLocation) {
        this.crosshair.x = mouseLocation.x;
        this.crosshair.y = mouseLocation.y;
    }

};

Duckhunt.DuckSystem = function (game, params) {
    this.worldWidth = params.width;
    this.worldHeight = params.height;
    this.duckImageKey = params.duckImageKey;
    this.duckWidth = game.cache.getImage(this.duckImageKey).width;
    this.timeBetweenDucks = params.timeBetweenDucks;
    this.lines = {
        0: this.worldHeight * 0.6,
        1: this.worldHeight * 0.65,
        2: this.worldHeight * 0.7,
        3: this.worldHeight * 0.78
    };
    this.ducks = [];
    this.game = game;
};
Duckhunt.DuckSystem.prototype = {

    start: function () {
        this.duckSpawnTimer = this.game.time.create(false);
        this.duckSpawnTimer.loop(this.timeBetweenDucks, this.spawnDuck, this);
        this.duckSpawnTimer.start();
    },

    spawnDuck: function () {
        var duck = new Duckhunt.Duck(this.game, {
            x: this.worldWidth + this.game.rnd.integerInRange(20, 200),
            y: this.lines[this.game.rnd.integerInRange(0, 3)],
            speed: this.game.rnd.integerInRange(2, 5),
            imageKey: 'duck'
        });
        this.ducks.push(duck);
    },

    update: function () {
        this.ducks.forEach(function (duck) {
            duck.update();
        });
    }
};

Duckhunt.Duck = function (game, params) {
    this.speed = params.speed;
    this.sprite = game.add.sprite(params.x, params.y, params.imageKey);
};
Duckhunt.Duck.prototype = {

    update: function () {
        this.sprite.x -= this.speed;
    }

};

game.state.add('Duckhunt.PreLoad', Duckhunt.PreLoad, true);
game.state.add('Duckhunt.MainMenu', Duckhunt.MainMenu);
game.state.add('Duckhunt.Game', Duckhunt.Game);
