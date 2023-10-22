package frame;

public class User{
    private String username;
    private String passward;

    //标准javabean类
    public User() {
    }
    public User(String username, String passward) {
        this.username = username;
        this.passward = passward;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassward() {
        return passward;
    }
    public void setPassward(String passward) {
        this.passward = passward;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        result = prime * result + ((passward == null) ? 0 : passward.hashCode());
        return result;
    }
    //用来比较两个对象的值是否相等
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        if (passward == null) {
            if (other.passward != null)
                return false;
        } else if (!passward.equals(other.passward))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "username="+username+"&password="+passward;
    }
    
    
}