package model;

public class User {
    private String userId;
    private String password;
    private String name;
    private String email;

    public User(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }
    
    @Override
    public boolean equals(Object obj) {
    	
    	String userId = "";
    	String password = "";
    	String name = "";
    	String email = "";
    	
    	if (obj instanceof User) {
			User user = (User) obj;
			userId = user.getUserId();
			password = user.getPassword();
			name = user.getName();
			email = user.getEmail();
		}
    	
    	return this.userId.equals(userId) && this.password.equals(password) && this.name.equals(name) && this.email.equals(email);
    }
}
