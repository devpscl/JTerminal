package net.jterminal.ui.event.special;

import net.jterminal.ui.component.selectable.SliderComponent;
import org.jetbrains.annotations.NotNull;

public class SliderChangeEvent extends AbstractComponentEvent<SliderComponent> {

  private final float value;

  public SliderChangeEvent(@NotNull SliderComponent component, float v) {
    super(component);
    this.value = v;
  }

  public float value() {
    return value;
  }
}
