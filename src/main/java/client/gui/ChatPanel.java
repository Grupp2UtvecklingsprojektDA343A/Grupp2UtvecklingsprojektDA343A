package client.gui;

import client.IChat;

public class ChatPanel extends DefaultPanel implements IChat {
    public ChatPanel(MainWindow gui, boolean showMenuBar) {
        super(gui, showMenuBar);
    }

    @Override
    public void closeApplication() {
        //TODO
    }
}
