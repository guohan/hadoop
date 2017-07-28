package hibernate;

/*
 * drop table if exists User;
 * create table if not exists User(id int ,name string) clustered by(id) into 10 buckets stored as orc tblproperties("transactional"="true");
 * insert into table User(id,name) values(1,'bily')
 */
public class User {
  private String id;
  private String name;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
