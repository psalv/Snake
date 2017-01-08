import java.awt.*;


public class Square extends SquareComponent
{
	// constants
	private static final Color BODY_COLOR = Color.BLACK;
	private static final Color WALL_COLOR = Color.BLACK;
	private static final Color NONE_COLOR = Color.WHITE;

	private static final Color FOOD_COLOR = Color.BLUE;


	public static enum SquareType{BODY, NONE, FOOD, WALL};
	
	
	// Attributes	
	private SquareType type;    // Stores the type of Square this currently is

    private Square next;

	
/**
 * Create a Square tile of the specified type
 * @param t the SquareType to create
 */
	public Square(SquareType t){
		this.type = t;

		//set the initial color based on the initial type
		this.setColor();
		//allocate space for the neighbor array
	}


    public void setType(SquareType t){
        type = t;
        this.setColor();
    }

    public SquareType getType(){
        return type;
    }

    public void setNext(Square s){
        next = s;
    }

	public Square getNext(){
        return next;
    }

	/**
	 * Helper method to set the current tile color based on the 
	 * type of tile.
	 * @param t The type to use to set the color
	 */
	private void setColor(){
        SquareType t = type;
		switch(t){
		case BODY:
			this.setBackground(BODY_COLOR);
			break;
		case WALL:
			this.setBackground(WALL_COLOR);
			break;
		case FOOD:
			this.setBackground(FOOD_COLOR);
			break;
		case NONE:
			this.setBackground(NONE_COLOR);
			break;
		default:
			this.setBackground(NONE_COLOR);
			break;
		}
		if(type == SquareType.WALL)
			this.setForeground(Color.BLACK);
		else
			this.setForeground(Color.WHITE);
	}

	public String toString(){
		if(type == SquareType.BODY){
			return "body";
		}
		else if(type == SquareType.FOOD){
			return "food";
		}
		else if(type == SquareType.WALL){
			return "wall";
		}
		else{
			return "none";
		}
	}

}