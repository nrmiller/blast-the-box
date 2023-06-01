package net.nicksneurons.blastthebox.utils;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
* @author Nick Miller
*
* GLCamera.java 
*
* This class acts as a camera for an OpenGL context.
* It contains the location of the physical camera and 
* direction is it looking at. It also will provide a method
* to allow for automatically updating the view.
*/

public class Camera
{
	//Location variables
	private float x =0, y =0, z =0;
	//Focus Point Location.
	private float xL =0, yL =0, zL =1;
	private float renderDistance = 10; //This controls how far away the user is viewing.

	//Yaw = Y, Pitch = X, Roll  = Z.
	//radian values for yaw, pitch, and roll.
	private float yaw = (float)Math.toRadians(-90), pitch =0, roll = (float) Math.toRadians(90); 

	private final Vector3f upVector = new Vector3f(0.0f, 1.0f, 0.0f);
	
	//min's/max's for yaw pitch and roll
	private double pitchMin = -90, pitchMax = -90;
	private boolean pitchRestricted = false;
	
	public Camera()
	{
		this(0, 0, 0);
	}

	public Camera(float xLoc, float yLoc, float zLoc)
	{
		setPosition(xLoc, yLoc, zLoc);
	}
	
	public void setPosition(float xLoc, float yLoc, float zLoc)
	{
		x = xLoc;
		y = yLoc;
		z = zLoc;
	}
	
	public void setFocusPoint(float xLoc, float yLoc, float zLoc)
	{
		xL = xLoc;
		yL = yLoc;
		zL = zLoc;
	}

	public void moveForward(float magnitude)
	{
		//this method looks at the yaw, pitch, and roll, then calculates 
		//new x, y, z coordinates using the magnitude.
		
		float currentX = x;
		float currentY = y;
		float currentZ = z;

		float xMovement = (float) (magnitude * Math.cos(yaw) * Math.cos(pitch));
		float yMovement = (float) (magnitude * Math.sin(pitch));
		float zMovement = (float) (magnitude * Math.sin(yaw) * Math.cos(pitch));

		float xNew = currentX + xMovement;
		float yNew = currentY + yMovement;
		float zNew = currentZ + zMovement;

		setPosition(xNew, yNew, zNew);
		updateFocusPoint();
	}

	public void moveBackward(float magnitude)
	{
		//this method does the same as move forward, but in the opposite direction.
		moveForward(-1 * magnitude);
	}
	
	public void strafeForward(float magnitude)
	{
		//moves forward along XZ plane.
		float currentX = x;
		float currentZ = z;

		float xMovement = (float) (magnitude * Math.cos(yaw));
		float zMovement = (float) (magnitude * Math.sin(yaw));

		float xNew = currentX + xMovement;
		float zNew = currentZ + zMovement;

		setPosition(xNew, y, zNew); //don't change y
		updateFocusPoint();
	}
	
	public void strafeBackward(float magnitude)
	{
		strafeForward(-1 * magnitude);
	}
	
	public void strafeLeft(float magnitude)
	{
		//this method does the same as strafe right, but with a negative magnitude
		strafeRight(-1 * magnitude);
	}

	public void strafeRight(float magnitude)
	{
		//this method rotates the camera 90 degrees (ignoring roll)
		//then moves the camera forward by the magnitude.
		//Finally, the camera resumes its prior orientation.
		yaw+=Math.toRadians(90);
		x+= (float) (magnitude * Math.cos(yaw));
		z+= (float) (magnitude * Math.sin(yaw));
		yaw-=Math.toRadians(90);
		updateFocusPoint();
	}
	
	//rotating along the x-axis changes the pitch
	public void rotateX(float radians)
	{
		pitch+= radians;
		if(isPitchRestricted())
		{
			if(pitch>=pitchMax)
			{
				pitch = (float) pitchMax;
			}
			if(pitch<=pitchMin)
			{
				pitch = (float) pitchMin;
			}
		}
		updateFocusPoint();
	}
	public void rotateX(double degrees)
	{
		float radians = (float) Math.toRadians(degrees);
		rotateX(radians);
	}

	//rotating along the y-axis changes the yaw
	public void rotateY(float radians)
	{
		yaw+=radians;
		updateFocusPoint();
	}
	public void rotateY(double degrees)
	{
		float radians = (float) Math.toRadians(degrees);
		rotateY(radians);
	}

	//rotating along the z-axis changes the roll
	public void rotateZ(float radians)
	{
		roll+=radians;
		updateUpVector();
		updateFocusPoint();
	}
	public void rotateZ(double degrees)
	{
		float radians = (float) Math.toRadians(degrees);
		rotateZ(radians);
	}
	
	//change the up reference point based on roll
	private void updateUpVector()
	{
		upVector.x = (float) Math.cos(roll);
		upVector.y = (float) Math.sin(roll);
	}
	//this method updates the look positions
	private void updateFocusPoint()
	{
		xL = (float) (Math.cos(yaw) * Math.cos(pitch) * renderDistance + x);
		yL = (float) Math.sin(pitch) * renderDistance + y;
		zL= (float) (Math.sin(yaw) * Math.cos(pitch) * renderDistance + z);
		
	}

	public Matrix4f getViewMatrix()
	{
		return new Matrix4f().lookAt(x, y, z, xL, yL, zL, upVector.x, upVector.y, upVector.z);
	}
	
	public void setPitchRestrictions(double radMin, double radMax)
	{
		pitchRestricted = true;
		pitchMin = radMin;
		pitchMax = radMax;
	}
	
	public double getMinimumPitch()
	{
		return pitchMin;
	}
	public double getMaximumPitch()
	{
		return pitchMax;
	}
	public boolean isPitchRestricted()
	{
		return pitchRestricted;
	}
	
	//GETS
	public float getXPos()
	{
		return x;
	}
	public float getYPos()
	{
		return y;
	}
	public float getZPos()
	{
		return z;
	}
	public float getXLPos()
	{
		return xL;
	}
	public float getYLPos()
	{
		return yL;
	}
	public float getZLPos()
	{
		return zL;
	}
	public Vector3f getUpVector()
	{
		return upVector;
	}
	public float getPitch()
	{
		return pitch;
	}
	public float getYaw()
	{
		return yaw;
	}
	public float getRoll()
	{
		return roll;
	}
	public float getRenderingDistance()
	{
		return renderDistance;
	}
	//SETS
	public void setXPos(float x)
	{
		this.x = x;
	}
	public void setYPos(float y)
	{
		this.y = y;
	}
	public void setZPos(float z)
	{
		this.z = z;
	}
	public void setXLPos(float x)
	{
		xL = x;
	}
	public void setYLPos(float y)
	{
		yL = y;
	}
	public void setZLPos(float z)
	{
		zL = z;
	}
	public void setRenderingDistance(float distance)
	{
		renderDistance = distance;
	}
	
}





