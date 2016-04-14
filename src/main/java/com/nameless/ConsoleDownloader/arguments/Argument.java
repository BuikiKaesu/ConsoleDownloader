package com.nameless.ConsoleDownloader.arguments;

class Argument {
    private String value;
    final private String flag;
    final private String description;
    final private boolean isRequired;

    private Argument(final Builder builder) {
        this.flag = builder.flag;
        this.description = builder.description;
        this.isRequired = builder.isRequired;
        this.value = builder.value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public String getFlag() {
        return flag;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public String getDescription() {
        return description;
    }

    public static class Builder {
        final private String flag;
        final private String description;
        private boolean isRequired = false;
        private String value;

        public Builder(final String flag, final String description) {
            this.flag = flag;
            this.description = description;
        }

        public Builder isRequired(final boolean isRequired) {
            this.isRequired = isRequired;
            return this;
        }

        public Builder defaultValue(final String value) {
            this.value = value;
            return this;
        }

        public Argument build() {
          return new Argument(this);
        }
    }
}
