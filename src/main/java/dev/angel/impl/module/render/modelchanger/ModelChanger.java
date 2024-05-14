package dev.angel.impl.module.render.modelchanger;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.value.NumberValue;

//TODO: rotates and no eating
public class ModelChanger extends Module {

    protected final NumberValue<Float> translateX =
            new NumberValue<>(new String[]{"TranslateX", "transx"},
                    "Translation of the X axis on both hands.",
                    0F, -2F, 2F, 0.1F
            );

    protected final NumberValue<Float> translateY =
            new NumberValue<>(new String[]{"TranslateY", "transy"},
                    "Translation of the Y axis on both hands.",
                    0F, -2F, 2F, 0.1F
            );

    protected final NumberValue<Float> translateZ =
            new NumberValue<>(new String[]{"TranslateZ", "transz"},
                    "Translation of the Z axis on both hands.",
                    0F, -2F, 2F, 0.1F
            );

    protected final NumberValue<Float> scaleX =
            new NumberValue<>(new String[]{"ScaleX", "scalx"},
                    "Scale of the X axis on both hands.",
                    1F, 0F, 2F, 0.1F
            );

    protected final NumberValue<Float> scaleY =
            new NumberValue<>(new String[]{"ScaleY", "scaly"},
                    "Scale of the Y axis on both hands.",
                    1F, 0F, 2F, 0.1F
            );

    protected final NumberValue<Float> scaleZ =
            new NumberValue<>(new String[]{"ScaleZ", "scalz"},
                    "Scale of the Z axis on both hands.",
                    1F, 0F, 2F, 0.1F
            );
    
    public ModelChanger() {
        super("ViewModel", new String[]{"viewmodel", "modelchanger", "niggachanger", "viewmod"}, "Changes our viewmodel", Category.RENDER);
        this.offerValues(translateX, translateY, translateZ, scaleX, scaleY, scaleZ);
        this.offerListeners(new ListenerHeldItemRender(this));
    }
}
