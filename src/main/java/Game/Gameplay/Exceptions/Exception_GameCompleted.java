package Game.Gameplay.Exceptions;

public class Exception_GameCompleted extends RuntimeException {
    public Exception_GameCompleted() {
        super(
            "<html>" +
            "   <body>" +
            "       <div width='300px' align='left'>" +
            "           <font color='green'>Congratulations!</font>" +
            "           <br>You have finished the game!" +
            "           <br><br>Do you want to play again?" +
            "       </div>" +
            "   </body>" +
            "</html>"
        );
    }
}
