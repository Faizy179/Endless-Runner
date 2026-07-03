const canvas = document.getElementById("gameCanvas");
const ctx = canvas.getContext("2d");

const GRAVITY = 1;
const JUMPSTRENGTH = -15;
const GROUNDY = 250;
const jumpSound = new Audio("jumpSFX.mp3");
const gameOverSound = new Audio("gameOver.mp3");
const OBSTACLE_SIZE = [
{
    w:20, h:30
},
{
    w:30, h :40
},
{
    w:40, h:60
},
{
    w:50, h:75 
}
];
let playerY = 250;
let playerVelocity = 0;
let obstacleX = 450;
let currentObstacle = OBSTACLE_SIZE[0];
let obstacleSpeed = 5;
let score = 0;
let highScore = localStorage.getItem("neonRunnerHighScore")|| 0;
let gameOver = false;
document.addEventListener("keydown", (event) => {
    if(event.code === "Space" ) {
        if(gameOver){

            resetGame();
        }
        else if(playerY === GROUNDY){
            jumpSound.currentTime = 0;
            jumpSound.play();
            playerVelocity = JUMPSTRENGTH;
        }
    } 
});
function resetGame(){
    playerY = GROUNDY;
    score = 0;
    playerVelocity = 0;
    obstacleX = 460;
    obstacleSpeed = 6;
    gameOver = false;
    currentObstacle = OBSTACLE_SIZE[0];
}
let lastTime = performance.now();
let delta = 0;
const Fps = 60;
const timePerFrame  = 1000/Fps;
let thisFrame = 0;
let lastFrameTime = 0;
let currentFramesPerSecond = 0;
function gameLoop(currentTime){
    if(lastTime === 0){ 
        lastTime = currentTime;
    }
    if(currentTime > (lastFrameTime + 1000)){
        currentFramesPerSecond = thisFrame;
        thisFrame = 0;
        lastFrameTime = currentTime;
    } 
    thisFrame++;
    delta+= (currentTime - lastTime) / timePerFrame;
    lastTime = currentTime;
    if(delta > 10){
        delta = 1;
    }
    while(delta >= 1){
        updateLogic();
        delta--;
    }
    render(delta);
    requestAnimationFrame(gameLoop);
}
function updateLogic(){
    if(!gameOver){
        playerVelocity += GRAVITY;
        playerY += playerVelocity;
        if(playerY >= GROUNDY){
            playerY = GROUNDY;
            playerVelocity = 0;
        }
        obstacleX -= obstacleSpeed;
        if(obstacleX < -currentObstacle.w){
            obstacleX = 460;
            score++;
             if(score > highScore){
                highScore = score;
                localStorage.setItem("neonRunnerHighScore", highScore);
            }
            let currentLevel = Math.floor(score/5) + 1;
            let maxIndex = Math.min(currentLevel,4) -1;
            let randomIndex = Math.floor(Math.random()*(maxIndex+1));
            currentObstacle = OBSTACLE_SIZE[randomIndex];
            if(score % 5 === 0){
                obstacleSpeed++;
            }

        }
        let obsY = (GROUNDY + 30) - currentObstacle.h;
        if(50 < (obstacleX + currentObstacle.w) &&  (80) > obstacleX && playerY < (obsY + currentObstacle.h) && playerY + 30 > obsY){
           gameOverSound.currentTime = 0;
           gameOverSound.play();
            gameOver = true;
        }
    }  
}
function render(interp = 0){
    ctx.fillStyle = "rgb(15, 15, 20)";    
    ctx.fillRect(0, 0, canvas.width, canvas.height);
    let smoothPlayerY = playerY;
    let smoothObstacleX = obstacleX;
    if(!gameOver){
        smoothPlayerY +=playerVelocity * interp;
        smoothObstacleX -= obstacleSpeed *interp;
    }
    ctx.strokeStyle = "rgba(0, 255, 255, 0.4)"; 
    ctx.lineWidth = 1;
    ctx.beginPath();
    ctx.moveTo(0, GROUNDY + 30);
    ctx.lineTo(canvas.width, GROUNDY + 30);
    ctx.stroke();

    ctx.fillStyle = "rgb(0, 255, 255)";
    if (ctx.roundRect) {
        ctx.beginPath();
        ctx.roundRect(50, smoothPlayerY, 30, 30, 10);
        ctx.fill();
    } else {
        ctx.fillRect(50, smoothPlayerY, 30, 30);
    }
    let obsY = (30+GROUNDY) - currentObstacle.h;
    ctx.fillStyle = "rgb(255, 0, 128)";
    ctx.fillRect(smoothObstacleX, obsY,currentObstacle.w,currentObstacle.h);

    ctx.fillStyle = "white";
    ctx.font = "bold 22px Monospace";
    ctx.fillText("SCORE: " + score, 20, 40);
    ctx.fillText("HIGH SCORE: " + highScore,20,70)
    ctx.fillStyle = "lime";
    ctx.font = "bold 16px Monospace";
    ctx.fillText("FPS: " + currentFramesPerSecond, (canvas.width - 90), 30);
    if (gameOver) {
        ctx.fillStyle = "rgba(0, 0, 0, 0.6)";
        ctx.fillRect(0, 0, canvas.width, canvas.height);

        ctx.fillStyle = "white";
        ctx.font = "bold 36px Monospace";
        ctx.fillText("GAME OVER", 50, 150);
        
        ctx.font = "16px Monospace";
        ctx.fillText("> Press SPACE to restart", 70, 190);
    }
}
requestAnimationFrame(gameLoop);