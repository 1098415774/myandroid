package com.example.myapplication.solution;

public class EquipAction {

    private EquipAction nextaction;

    private String equipId;

    private String actionid;

    public EquipAction getNextaction() {
        return nextaction;
    }

    public void setNextaction(EquipAction nextaction) {
        this.nextaction = nextaction;
    }

    public String getEquipId() {
        return equipId;
    }

    public void setEquipId(String equipId) {
        this.equipId = equipId;
    }

    public String getActionid() {
        return actionid;
    }

    public void setActionid(String actionid) {
        this.actionid = actionid;
    }
}
