package realestateempire.singleplayer.board.street;

public enum BuildingState {

    NO_HOUSES(0) {
        @Override
        public BuildingState getPrevious() {
            return this;
        }
    },
    ONE_HOUSE(1),
    TWO_HOUSES(2),
    THREE_HOUSES(3),
    FOUR_HOUSES(4),
    HOTEL(5) {
        @Override
        public BuildingState getNext() {
            return this;
        }
    };

    private static final BuildingState[] values = values();
    private final int buildingLevel;

    BuildingState(int buildingLevel) {
        this.buildingLevel = buildingLevel;
    }

    public int getBuildingLevel() {
        return buildingLevel;
    }

    public BuildingState getPrevious() {
        return values[(ordinal() - 1 + values.length) % values.length];
    }

    public BuildingState getNext() {
        return values[(ordinal() + 1) % values.length];
    }
}
