/**
 * Habits is the object class which allows an application to give
 * a habit to reduce carbon emissions.
 * A Habit object encapsulates the state information needed
 * for the various information for a habit. This
 * state information includes:
 * <ul>
 * <li>The category of the habit to sort it
 * <li>Progress to track the user's progress
 * <li>The act, what the user has to do
 * <li>The frequency, how frequent the user has to do the act
 * <li>The impact, the carbon impact of the act
 * </ul>
 *
 * @author      Ronald Ng
 * @since       1.0
 */

public class Habit {
	private String category;
	private double progress;
	private String act;
	private double frequency;
	private double impact;


	/**
	 * Creates habit object
	 *
	 * @param category       The category of the habit to sort it
	 * @param progress       Progress to track the user's progress
	 * @param impact         The impact, the carbon impact of the act
	 * @param act            The act, what the user has to do
	 * @param frequency      The frequency, how frequent the user has to do the act
	 * @return          habit object
	 * @since           1.0
	 */

	public Habit(String category, double progress, double impact, String act, double frequency){
		this.category = category;
		this.progress = progress;
		this.act = act;
		this.frequency = frequency;
		this.impact = impact;
	}

	/**
	 * gives category
	 *
	 * @return          category
	 * @since           1.0
	 */

	public String getCategory() {
		return category;
	}

	/**
	 * gives act
	 *
	 * @return          act
	 * @since           1.0
	 */

	public String getAct() {
		return act;
	}

	/**
	 * gives progress
	 *
	 * @return          progress
	 * @since           1.0
	 */

	public double getProgress() {
		return progress;
	}

	/**
	 * gives frequency
	 *
	 * @return          frequency
	 * @since           1.0
	 */
	public double getFrequency() {
		return frequency;
	}

	/**
	 * gives impact
	 *
	 * @return          impact
	 * @since           1.0
	 */
	public double getImpact() {
		return impact;
	}

	/**
	 * Changes category
	 *
	 * @param category		new category
	 * @since           1.0
	 */

	public void setCategory(String category){
		this.category = category;
	}

	/**
	 * Changes act
	 *
	 * @param act		new act
	 * @since           1.0
	 */
	public void setAct(String act){
		this.act = act;
	}


	/**
	 * Changes act
	 *
	 * @param act		new act
	 * @since           1.0
	 */
	public void setProgress(double progress){
		this.progress = progress;
	}

	/**
	 * Changes frequency
	 *
	 * @param frequency		new frequency
	 * @since           1.0
	 */

	public void setFrequency(double frequency){
		this.frequency = frequency;
	}

	/**
	 * Changes impact
	 *
	 * @param impact		new impact
	 * @since           1.0
	 */
	public void setImpact(double impact){
		this.impact = impact;
	}

	/**
	 * Updates progress
	 *
	 * @param update		amount progress that needs to be updated by
	 * @since           1.0
	 */
	public void updateProgress(double update){
		this.progress = this.progress + update;
	}

}