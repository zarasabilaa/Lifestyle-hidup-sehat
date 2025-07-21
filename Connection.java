import java.sql.PreparedStatement;
import java.sql.Statement;

public class Connection {

    public Statement createStatement() {
        return null;
    }

    public PreparedStatement prepareStatement(String string) {
        
        throw new UnsupportedOperationException("Unimplemented method 'prepareStatement'");
    }

    public void close() {
        
        throw new UnsupportedOperationException("Unimplemented method 'close'");
    }

}
