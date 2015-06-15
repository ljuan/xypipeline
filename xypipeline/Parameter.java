package xypipeline;

class Parameter {
	private String ID;
	private String Type;
	private String Option;
	private String Default;
	Parameter(String ID, String Type, String Option, String Default){
		this.ID = ID;
		this.Type = Type;
		this.Option = Option;
		this.Default = Default;
	}
	String getID(){
		return ID;
	}
	String getType(){
		return Type;
	}
	String getOption(){
		return Option;
	}
	String getDefault(){
		return Default;
	}
	boolean ifRequired(){
		if("".equals(Default) && !"".equals(Type))
			return true;
		else
			return false;
	}
}