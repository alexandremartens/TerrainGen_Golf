package entities;

import com.badlogic.gdx.math.Vector2;
import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import terrains.Terrain;
import toolbox.EulerMethod;

public class Player extends Entity{

    private static final float RUN_SPEED = 20;
    private static final float TURN_SPEED = 160;
    private static final float GRAVITY = -50;
    private static final float JUMP_POWER = 30;


    private static Vector2 veloVector = new Vector2(5,5);


    private float currentSpeed = 5;
    private float currentTurnSpeed = 0;
    private float upwardSpeed = 0;

    private boolean inAir = false;

    public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
        this.setCurrentSpeed(5f);
    }

    public void move(Terrain terrain){
        checkInputs();
        super.increaseRotation(0, currentTurnSpeed*DisplayManager.getFrameTimeSeconds(),0);

        float distance = currentSpeed*DisplayManager.getFrameTimeSeconds();
        float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
        float dy = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));

        super.increasePosition(dx,0,dy);

        upwardSpeed += GRAVITY*DisplayManager.getFrameTimeSeconds();
        super.increasePosition(0, upwardSpeed*DisplayManager.getFrameTimeSeconds(), 0);

        float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
        //TODO collisionshit is here
        if (super.getPosition().y < terrainHeight){
            upwardSpeed = 0;
            inAir = false;
            super.getPosition().y = terrainHeight;
        }
    }

    public void updateBall(){


        System.out.println(currentSpeed);
        Vector2 pos = new Vector2(getPosition().x,getPosition().z);
        Vector2 velo = new Vector2(getVeloVector().x, getVeloVector().y);

        Vector2 newVelo = EulerMethod.newVelocity(pos, velo);
        setVeloVector(newVelo);

        Vector2 newPos = EulerMethod.newPosition(new Vector2(getPosition().x, getPosition().z), new Vector2(getVeloVector().x, getVeloVector().y));
        setPosition(new Vector3f(newPos.x,0,newPos.y));

        float newVelocity = (float) Math.sqrt(Math.pow(newVelo.x, 2)+Math.pow(newVelo.y, 2));
        setCurrentSpeed(newVelocity);

        //entities.ball.moveBall(newPos.x, newPos.y);


        //System.out.println(getPosition());
    }


    private void jump(){
        if (!inAir) {
            this.upwardSpeed = JUMP_POWER;
            this.inAir = true;
        }
    }

    private void checkInputs(){
        if (Keyboard.isKeyDown(Keyboard.KEY_W)){
            this.currentSpeed = RUN_SPEED;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S)){
            this.currentSpeed = -RUN_SPEED;
        }else{
            this.currentSpeed = 0;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_D)){
            this.currentTurnSpeed = -TURN_SPEED;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_A)){
            this.currentTurnSpeed = TURN_SPEED;
        } else {
            this.currentTurnSpeed = 0;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            jump();
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
            System.out.println("currentSpeed = " + currentSpeed);
            updateBall();
            //super.setPosition(new Vector3f(200,100,-100));
        }
    }

    public float getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(float currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public static Vector2 getVeloVector() {
        return veloVector;
    }

    public static void setVeloVector(Vector2 veloVector) {
        Player.veloVector = veloVector;
    }
    public void setPosition(Vector3f vector){
        super.setPosition(vector);
    }
}
