const canvas = document.getElementById("gameCanvas");
const ctx = canvas.getContext("2d");

const GRAVITY = 1;
const JUMPSTRENGTH = -15;
const GROUNDY = 250;

let playerY = 250;
let playerVelocity = 0;
let obstacleX = 450;
let obstacleSpeed = 5;
let score = 0;
let gameOver = false;
document.addEventListener("keydown", (event) => {
    if(event.code === "Space" ) {
        if(gameOver){
            resetGame();
        }
        else if(playerY === GROUNDY){
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
}
function gameLoop(){
    updateLogic();
    render();
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
        if(obstacleX < -20){
            obstacleX = 460;
            score++;
            if(score % 5 === 0){
                obstacleSpeed++;
            }
        }
        if(50 < obstacleX + 20 &&  50 + 30 > obstacleX && playerY < GROUNDY + 30 && playerY + 30 > GROUNDY)}{
            gameOver = true;
        }
    }
}
function render(){
    ctx.fillStyle = "rgb(15, 15, 20)";
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    ctx.strokeStyle = "rgba(0, 255, 255, 0.4)"; 
    ctx.lineWidth = 1;
    ctx.beginPath();
    ctx.moveTo(0, GROUNDY + 30);
    ctx.lineTo(canvas.width, GROUNDY + 30);
    ctx.stroke();

    ctx.fillStyle = "rgb(0, 255, 255)";
    if (ctx.roundRect) {
        ctx.beginPath();
        ctx.roundRect(50, playerY, 30, 30, 10);
        ctx.fill();
    } else {
        ctx.fillRect(50, playerY, 30, 30);
    }

    ctx.fillStyle = "rgb(255, 0, 128)";
    ctx.fillRect(obstacleX, GROUNDY, 20, 30);

    ctx.fillStyle = "white";
    ctx.font = "bold 22px Monospace";
    ctx.fillText("SCORE: " + score, 20, 40);

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