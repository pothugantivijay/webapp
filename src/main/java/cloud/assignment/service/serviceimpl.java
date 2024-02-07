package cloud.assignment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.sql.Connection;

@Service
public class serviceimpl implements connection{

    @Autowired
    private DataSource dataSource;
    @Override
    public boolean isconnectionok(){
        try(Connection connection = dataSource.getConnection()){
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

}
