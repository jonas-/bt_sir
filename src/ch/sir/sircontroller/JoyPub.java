package ch.sir.sircontroller;

import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import android.util.Log;

public class JoyPub extends AbstractNodeMain {
	
	public GraphName getDefaultNodeName() {
		return GraphName.of("joy");
	}
//	
//	@Override
//	public void onStart(final ConnectedNode connectedNode) {
//		Log.i("info", "started");
//		final Publisher<std_msgs.String> publisher =
//				connectedNode.newPublisher("/cmd_vel", geometry_msgs.Twist._TYPE);
//		
//	}
}
