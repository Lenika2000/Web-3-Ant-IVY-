package app.Model;

import app.Entities.Point;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class GraphicTest {
    Graphic graphic;
    Point point;
    @Before
    public void init() {
        graphic = new Graphic();
    }

    //проверка точности попадания в верхней точке треугольника
    @Test
    public void isHitTriangleTop1() {
        point=new Point(new BigDecimal(0),new BigDecimal(3.99),4,0);
        boolean expected = graphic.isHit(point,4);
        assertEquals(expected,true);
    }

    @Test
    public void isHitTriangleTop2() {
        point=new Point(new BigDecimal(0),new BigDecimal(4.01),4,0);
        boolean expected = graphic.isHit(point,4);
        assertEquals(expected,false);
    }

    @Test
    public void isHitTriangleRight1() {
        point=new Point(new BigDecimal(3.01),new BigDecimal(0),3,0);
        boolean expected = graphic.isHit(point,3);
        assertEquals(expected,false);
    }


    @Test
    public void isHitTriangleRight2() {
        point=new Point(new BigDecimal(2.99),new BigDecimal(0),3,0);
        boolean expected = graphic.isHit(point,3);
        assertEquals(expected,true);
    }



    @Test
    public void isHiCircleBottom1() {
        point=new Point(new BigDecimal(-1.01),new BigDecimal(0),2,0);
        boolean expected = graphic.isHit(point,2);
        assertEquals(expected,false);
    }


    @Test
    public void isHiCircleBottom2() {
        point=new Point(new BigDecimal(-1),new BigDecimal(0),3,0);
        boolean expected = graphic.isHit(point,2);
        assertEquals(expected,false);
    }

    @After
    public void exit() {
        point= null;
        graphic = null;
    }

}