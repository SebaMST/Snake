package Game.Gameplay.Exceptions;

public class Exception_LevelCompleted extends RuntimeException {
    public Exception_LevelCompleted(String levelCompleted) {
        super(
            "<html>" +
            "   <body>" +
            "       <div width='300px' align='left'>" +
            "           <font color='green'>Congratulations!</font>" +
            "           <br>You have completed level "+levelCompleted+"!" +
            "           <br><br>Do you want to continue to the next level?" +
            "       </div>" +
            "   </body>" +
            "</html>"
        );
    }
}
