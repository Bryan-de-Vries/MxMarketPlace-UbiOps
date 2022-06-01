// This file was generated by Mendix Studio Pro.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package awsservices.actions;

import java.util.LinkedList;
import java.util.List;
import com.mendix.core.Core;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.webui.CustomJavaAction;
import awsservices.impl.AWSHelper;
import software.amazon.awssdk.regions.Region;

public class SyncRegions extends CustomJavaAction<java.lang.Boolean>
{
	public SyncRegions(IContext context)
	{
		super(context);
	}

	@java.lang.Override
	public java.lang.Boolean executeAction() throws Exception
	{
		// BEGIN USER CODE
		List<Region> regions = Region.regions();
		List<IMendixObject> newRegions = new LinkedList<>();
		
		for (Region region : regions) {
			List<IMendixObject> mxRegions = Core.retrieveXPathQuery(getContext(), "//" + 
					awsservices.proxies.Region.entityName + "[" + 
					awsservices.proxies.Region.MemberNames.Name.toString() + "='" + region.id() + "']");
			if (mxRegions.size() == 0) {
				AWSHelper.LOGGER.info("Creating region " + region.id());
				awsservices.proxies.Region newRegion = new awsservices.proxies.Region(getContext());
				newRegion.setName(region.id());
				newRegions.add(newRegion.getMendixObject());
			}
		}
		
		Core.commit(getContext(), newRegions);
		return true;		
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "SyncRegions";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}