package ute.player.role;

public class IRole {
    public int level;
    public int sanMax;
    public int healthMax;
    public double damageLevel;

    public IRole(int level, int sanMax, int healthMax, double damageLevel) {
        this.level = level;
        this.sanMax = sanMax;
        this.healthMax = healthMax;
        this.damageLevel = damageLevel;
    }
}
