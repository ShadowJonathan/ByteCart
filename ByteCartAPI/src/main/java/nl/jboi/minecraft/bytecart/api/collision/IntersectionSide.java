package nl.jboi.minecraft.bytecart.api.collision;


public interface IntersectionSide {

    /**
     * Position of the T cross-roads.
     */
    enum Side {
        LEVER_ON(7),
        LEVER_OFF(0);

        private int Value;

        Side(int b) {
            Value = b;
        }

        public int Value() {
            return Value;
        }

        public Side opposite() {
            if (this.equals(LEVER_OFF))
                return LEVER_ON;
            return LEVER_OFF;
        }
    }

}
