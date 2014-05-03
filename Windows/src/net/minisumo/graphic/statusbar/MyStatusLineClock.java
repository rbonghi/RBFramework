/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.graphic.statusbar;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.awt.StatusLineElementProvider;
import org.openide.util.Cancellable;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Raffaello
 */
@ServiceProvider(service = StatusLineElementProvider.class)
public class MyStatusLineClock implements StatusLineElementProvider {

    private static DateFormat format = DateFormat.getTimeInstance(DateFormat.MEDIUM);
    private static JLabel time = new JLabel(" " + format.format(new Date()) + " ");
    private JPanel panel = new JPanel(new BorderLayout());
//    ProgressHandle p;

    public MyStatusLineClock() {
        Timer t = new Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                time.setText(" " + format.format(new Date()) + " ");
            }
        });
        t.start();
//        Cancellable aaa = new Cancellable() {
//
//            @Override
//            public boolean cancel() {
//                p.finish();
//                return true;
//            }
//        };
//        p = ProgressHandleFactory.createHandle("My Task", aaa);
//        p.start(100);
//        Runnable run = new Runnable() {
//
//            @Override
//            public void run() {
////                ProgressHandle p = ProgressHandleFactory.createHandle("My Task");
////                p.switchToIndeterminate();
////                p.start(100);
//                // do some work
//                p.progress("Step 1", 10);
////                 do some more work
////                p.progress(100);
////                p.finish();
//            }
//        };
//        Thread t2 = new Thread(run);
//        t2.start(); // start the task and progress visualization
//        p.switchToIndeterminate();
        panel.add(new JSeparator(SwingConstants.VERTICAL), BorderLayout.WEST);
        panel.add(time, BorderLayout.CENTER);
    }

    @Override
    public Component getStatusLineElement() {
        return (panel);
    }
}
