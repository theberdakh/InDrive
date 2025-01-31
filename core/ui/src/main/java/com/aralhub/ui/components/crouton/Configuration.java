package com.aralhub.ui.components.crouton;

import androidx.annotation.NonNull;

public class Configuration {


  /**
   * Display a  for an infinite amount of time or
   * until Crouton cancel has been called.
   */
  public static final int DURATION_INFINITE = -1;
  /** The default short display duration of a Crouton. */
  public static final int DURATION_SHORT = 3000;
  /** The default long display duration of a Crouton. */
  public static final int DURATION_LONG = 5000;

  /** The default {@link Configuration} of a Crouton. */
  public static final Configuration DEFAULT;

  static {
    DEFAULT = new Builder().setDuration(DURATION_SHORT).build();
  }

  /** The durationInMilliseconds the Crouton will be displayed in milliseconds. */
  final int durationInMilliseconds;
  /** The resource id for the in animation. */
  final int inAnimationResId;
  /** The resource id for the out animation. */
  final int outAnimationResId;

  private Configuration(Builder builder) {
    this.durationInMilliseconds = builder.durationInMilliseconds;
    this.inAnimationResId = builder.inAnimationResId;
    this.outAnimationResId = builder.outAnimationResId;
  }

  /** Creates a {@link Builder} to build a {@link Configuration} upon. */
  public static class Builder {
    private int durationInMilliseconds = DURATION_SHORT;
    private int inAnimationResId = 0;
    private int outAnimationResId = 0;

    /**
     * Set the durationInMilliseconds option of theCrouton.
     *
     * @param duration
     *   The durationInMilliseconds the crouton will be displayed
     *   Crouton in milliseconds.
     *
     * @return the {@link Builder}.
     */
    public Builder setDuration(final int duration) {
      this.durationInMilliseconds = duration;

      return this;
    }

    /**
     * The resource id for the in animation.
     *
     * @param inAnimationResId
     *   The resource identifier for the animation that's being shown
     *   when the Crouton is going to be displayed.
     *
     * @return the {@link Builder}.
     */
    public Builder setInAnimation(final int inAnimationResId) {
      this.inAnimationResId = inAnimationResId;

      return this;
    }

    /**
     * The resource id for the out animation
     *
     * @param outAnimationResId
     *   The resource identifier for the animation that's being shown
     *   when the Crouton is going to be removed.
     *
     * @return the {@link Builder}.
     */
    public Builder setOutAnimation(final int outAnimationResId) {
      this.outAnimationResId = outAnimationResId;

      return this;
    }

    /**
     * Builds the {@link Configuration}.
     *
     * @return The built {@link Configuration}.
     */
    public Configuration build() {
      return new Configuration(this);
    }
  }

  @NonNull
  @Override
  public String toString() {
    return "Configuration{" +
      "durationInMilliseconds=" + durationInMilliseconds +
      ", inAnimationResId=" + inAnimationResId +
      ", outAnimationResId=" + outAnimationResId +
      '}';
  }
}