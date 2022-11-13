package com.mynguyen.projects.MealFlashSocialPlatform.model;

import com.mynguyen.projects.MealFlashSocialPlatform.security.oauth.AuthenticationProvider;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 45, name = "username", unique = true)
    @Size(min = 8, max = 16, message = "Username must be 8 to 16 characters long.")
    private String username;
//    H2 SQL syntax to add NOT NULL constraint to an existing column in User table: (The column
//    needs to be
//    manually added as spring.jpa.hibernate.ddl-auto is currently "update" (not "create") and the database won't be automatically updated as the entity classes change.
//    ALTER TABLE user ADD COLUMN username VARCHAR(45) NOT NULL DEFAULT 0;
//    without first setting the default value = 0 for the column to be added, H2 won't allow directly setting it to NOT NULL as the newly added column would already include NULL values by the time it was added.
//    ALTER TABLE user ALTER COLUMN username VARCHAR(45) NOT NULL;

//    Reverse H2 SQL syntax to remove NOT NULL constraint from an existing column:
//    alter table user alter column username varchar(45);
//    alter table user alter column username varchar(45) null;

    @Column(length = 64, name = "password", nullable = false)
    private String password;
//    ALTER TABLE user ADD COLUMN password VARCHAR(64) NOT NULL DEFAULT 0;
//    ALTER TABLE user ALTER COLUMN password VARCHAR(64) NOT NULL;

    private boolean enabled;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider")
    private AuthenticationProvider authProvider;

    @Column(length = 128, name = "fname", nullable = false)
    private String fname;

    @Column(length = 128, name = "lname", nullable = false)
    private String lname;

//    @Pattern(regexp = "^[a-zA-Z0-9.\\-\\/+=@_ ]*$", message = "")
    @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\" +
            ".[A-Za-z]{2,})$", message = "Email address contains illegal characters or follows an illegal pattern. " +
            "Allowed characters include Latin alphabet letters, general dot (.), underscore (_), hyphen (-) and " +
            "(@) sign.")
    @Column(length = 128, name = "email", nullable = false, unique = true)
    private String email;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    //Hibernate removes association entries very inefficiently, if mapping them as a List.
    //This can be avoided by mapping many-to-many association to a Set.

    @OneToMany(mappedBy = "me")
    private Set<MyFollower> followers;

    @OneToMany(mappedBy = "creator")/*(cascade = CascadeType.ALL, fetch = FetchType.EAGER)*/
    private List<Recipe> recipes;

    public User(){ }

    public User(Integer id) {
        this.id = id;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String fname, String lname, String email) {
        this.username = username;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
    }

    public User(String fname, String lname, String email) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
    }

    public User(String fname, String lname,
                String email, String password, AuthenticationProvider authProvider) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
        this.authProvider = authProvider;
    }

    public User(String username, String password, boolean enabled, String fname, String lname, String email) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
    }

//    Constructor for editing user
    public User(Integer id, String username, String password, String fname, String lname, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
//        this.enabled = true;
//        this.authProvider = AuthenticationProvider.LOCAL;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }
    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public AuthenticationProvider getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(AuthenticationProvider authProvider) {
        this.authProvider = authProvider;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role){
        this.roles.add(role);
    }

    public void removeRole(Role role){
        this.roles.remove(role);
    }

    public boolean hasRole(String roleTitle){
        Iterator<Role> iterator = roles.iterator();
        while(iterator.hasNext()){
            Role role = iterator.next();
            if(role.getTitle().equals(roleTitle)){
                return true;
            }
        }
        return false;
    }

    public Set<MyFollower> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<MyFollower> followers) {
        this.followers = followers;
    }

    public void addFollower(MyFollower follower){
        this.followers.add(follower);
    }
    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public void addRecipe(Recipe recipe){
        this.recipes.add(recipe);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return username;
//        return "" + id + "";
    }

}
