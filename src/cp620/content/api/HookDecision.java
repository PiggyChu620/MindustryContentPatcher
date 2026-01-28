package cp620.content.api;

public enum HookDecision
{
    Pass,
    ReturnTrue,
    ReturnFalse;

    public boolean isPass()
    {
        return this == Pass;
    }

    public boolean asBoolean()
    {
        return this == ReturnTrue;
    }
}
