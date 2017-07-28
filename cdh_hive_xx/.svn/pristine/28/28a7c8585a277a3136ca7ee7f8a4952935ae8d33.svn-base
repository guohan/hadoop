package hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
/*
 * 可以正常执行的 查询数据的行数
 * 
 */
public class InHibernate {
  public static void main(String[] args) {
//    String path = "transwarp\\io\\inceptor\\hibernate\\conf\\hibernate.cfg.xml";
    Configuration cfg = new Configuration().configure();
//    cfg.addResource(InHibernate.class.getClassLoader().getResource("transwarp/io/inceptor/hibernate/conf/hibernate.cfg.xml").getPath());
//    cfg.addResource(InHibernate.class.getClassLoader().getResource("transwarp/io/inceptor/hibernate/conf/User.hbm.xml").getPath());
    cfg.addResource("hibernate.cfg.xml");
    cfg.addResource("User.hbm.xml");

    SessionFactory sessionFactory = cfg.buildSessionFactory();
    {
      Session session = sessionFactory.openSession();
      session.beginTransaction();
/**
 *  insert into table User values ('test','505');
 *  add success
 */
      User user = new User();
      user.setId("44");
      user.setName("123");

//      session.save(user);
//      session.load(theClass, id)
//      session.createQuery("insert into table User values ('test123','502')");
      
      
      List<User> list = new ArrayList();
      String hql = "from User as u ";
      Query query = session.createQuery(hql);
      list= query.list();    //返回的是一个集合
      for (User object : list) {
		System.out.println(object.getName());
	}
      System.out.println(list.size());
//       session.close();
//      session.getTransaction().commit();
      sessionFactory.close();
    }
  }
}
