package com.example;

import com.google.inject.Provides;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;

import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.OverlayPosition;

@PluginDescriptor(
        name = "Visual Ticks 20",
        description = "Visual metronome de 20 ticks"
)
public class ExamplePlugin extends Plugin
{
    private static final int TOTAL_TICKS = 20;
    private static final int DIFFERENT_COLOR_START = 15;

    private static final Color NORMAL_COLOR = new Color(0, 200, 0);
    private static final Color LAST_TICKS_COLOR = new Color(220, 40, 40);
    private static final Color CURRENT_COLOR = Color.WHITE;

    @Inject
    private OverlayManager overlayManager;

    private int currentTick = 0;

    private final Overlay overlay = new Overlay()
    {
        {
            setPosition(OverlayPosition.DYNAMIC);
            setLayer(OverlayLayer.ABOVE_WIDGETS);
        }

        @Override
        public Dimension render(Graphics2D graphics)
        {
            int x = 20;
            int y = 20;

            int tickWidth = 18;
            int tickHeight = 18;
            int spacing = 3;

            for (int i = 0; i < TOTAL_TICKS; i++)
            {
                Color color = i >= DIFFERENT_COLOR_START
                        ? LAST_TICKS_COLOR
                        : NORMAL_COLOR;

                if (i == currentTick)
                {
                    color = CURRENT_COLOR;
                }

                int tickX = x + i * (tickWidth + spacing);

                graphics.setColor(color);
                graphics.fillRect(tickX, y, tickWidth, tickHeight);

                graphics.setColor(Color.BLACK);
                graphics.drawRect(tickX, y, tickWidth, tickHeight);
            }

            return null;
        }
    };

    @Provides
    VisualTicksConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(VisualTicksConfig.class);
    }

    @Override
    protected void startUp()
    {
        currentTick = 0;
        overlayManager.add(overlay);
    }

    @Override
    protected void shutDown()
    {
        overlayManager.remove(overlay);
    }

    @Subscribe
    public void onGameTick(GameTick event)
    {
        currentTick++;

        if (currentTick >= TOTAL_TICKS)
        {
            currentTick = 0;
        }
    }
}
