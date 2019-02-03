import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class Foo {
    static Logger logger = LogManager.getLogger(Foo.class);
    
    public Foo(){
        logger.info("Constructing foo");`
    }
    public String doStuff(Long x){
        logger.debug("doing stuff");
    } 
}