package app.Controller;

import app.Entities.Point;
import app.Entities.User;
import app.Model.Graphic;
import lombok.Data;

import javax.annotation.Resource;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@ManagedBean
@ApplicationScoped
@Data

/**
 * Класс для работы с базой данных, в которой хранится информация о точках, координаты которых получены из полей ввода
 * или путем выбора на графике.
 * @autor Lena
 * @version 1.1.0
 */
public class PointsTableBean implements Serializable {
    //Координата х из графического представления
    private BigDecimal XCanvas;
    //Координата y из графического представления
    private BigDecimal YCanvas;
    //Координата х, введенная с клавиатуры
    private BigDecimal x;
    //Координата y, введенная с клавиатуры
    private BigDecimal y;
    //Радиус r, введенный с клавиатуры (по умолч. 1)
    private double r = 1;
    //сдвиг таймзоны относительно сервера в минутах
    private int timezoneOffset;

    @PersistenceContext(unitName = "hibernate")
    private EntityManager em;

    @Resource
    private UserTransaction userTransaction;

    @ManagedProperty(value = "#{userBean}")
    private UserBean userBean;
    private User user;

    public String init() throws Exception {
        user = new User(userBean.getName());
        userBean.logIn();
        userTransaction.begin();
        em.merge(user);
        userTransaction.commit();
        return "toMain";
    }

    public PointsTableBean() {
        setR(1);
        setX(new BigDecimal(0));
    }

    //Добавление новой точки
    private void addPoint(Point point) {
        point.setUser(user);
        try {
            userTransaction.begin();
            em.persist(point);
            userTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //Добавление новой точки из формы ввода
    public void addPointFromForm()  {
        Point point = new Point(x, y, r, timezoneOffset);
        point.setCurrentTime(timezoneOffset);
        addPoint(point);
    }
    //Изменение радиуса для выбранной точки
    public void updatePoint(Point point) throws Exception {
        point.setR(r);
        point.setHit(Graphic.isHit(point, r));
        userTransaction.begin();
        em.merge(point);
        userTransaction.commit();
    }
    //Добавление новой точки с графика
    public void addPointFromCanvas() {
        addPoint(new Point(XCanvas, YCanvas, r, timezoneOffset));
    }

    /**Получение всех точек из бд
     *
     * @return возвращает отсортированные точки по времени добавления
     */
    public List<Point> getPoints() {
        Query query = em.createQuery("select p from Point p WHERE p.user = :id");
        query.setParameter("id", user);
        List<Point> points = (List<Point>) query.getResultList();
        points.sort((p1, p2) -> p1.getId() > p2.getId() ? 1 : -1);
        return points;
    }

}



