package cp620.content.api;

public enum HandleDecision
{
    Pass,
    Handled;

    public boolean isHandled()
    {
        return this == Handled;
    }
}
