package Game.Gameplay.Exceptions;

import Game.SnakeGame;

public class Exception_SnakeDead extends RuntimeException {
    public Exception_SnakeDead(String cause) {
        super(
            "<html>" +
            "   <body>" +
            "       <div width='300px' align='left'>" +
            "           Sorry! " +
            "           <br><br>The snake has <font color='red'>died</font> because "+cause+"!" +
            "           <br><br>You have <font color='" + (SnakeGame.getCountLives()-1 <= 0 ? "red" : "green") + "'>"+ (SnakeGame.getCountLives()-1) +"</font> lives remaining." +
            "           <br>" + (SnakeGame.getCountLives()-1 <= 0 ? "Do you want to start a new game?" : "Do you want to try this level again?") +
            "       </div>" +
            "   </body>" +
            "</html>"
        );
    }
}
