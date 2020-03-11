package HamsterYDS.UntilTheEnd.player.role;

public enum Roles {
	DEFAULT("未选择",0,200,20,1.0),
	WILSON("威尔逊",0,200,20,1.0),
	WILLOW("薇洛",0,120,20,1.0),
	WOLFGANG("沃尔夫冈",0,200,30,1.0),
	WENDY("温蒂",0,250,20,0.75),
	WX78("AI->WX-78",0,100,10,1.0),
	WICKERBOTTOM("维克波顿",0,250,20,1.0),
	WOODIE("伍迪",0,200,20,1.0),
	WES("维斯",0,150,15,0.75),
	WIGFRID("威戈芙瑞德",0,120,25,1.25),
	WEBBER("韦伯",0,100,22,1.0),
	MAXWELL("麦克斯韦",0,200,10,1.0);
	public String name;
	public int originLevel;
	public int originSanMax;
	public int originHealthMax;
	public double originDamageLevel;
	Roles(String name,int originLevel,int originSanMax,int originHealthMax,double originDamageLevel) {
		this.name=name;
		this.originLevel=originLevel;
		this.originSanMax=originSanMax;
		this.originHealthMax=originHealthMax;
		this.originDamageLevel=originDamageLevel;
	}
}
