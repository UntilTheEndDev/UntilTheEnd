package HamsterYDS.UntilTheEnd.player.role;

public enum Roles {
	DEFAULT("未选择"),
	WILSON("威尔逊"),
	WILLOW("薇落"),
	WOLFGANG("沃尔夫冈"),
	WENDY("温蒂"),
	WX78("AI->WX-78"),
	WICKERBOTTOM("维克波顿"),
	WOODIE("伍迪"),
	WES("维斯"),
	WIGFRID("威戈芙瑞德"),
	WEBBER("韦伯"),
	MAXWELL("麦克斯韦");
	String name;
	int level;
	Roles(String name) {
		this.name=name;
	}
}
