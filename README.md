__Design Ideas__
- project follows controller, handler, service pattern
- service is skipped since data in DataCenter here can be directly retrieved
- DataCenter.java models the exact behavior of a Database, all objects are deep copied during communication

__Run the Application__
- PostCommentApplication is the entry point, simply right click and run

__Security Config__
- csrf is disabled to allow requests from front-end

__Run Test__
- simply run mvn test
