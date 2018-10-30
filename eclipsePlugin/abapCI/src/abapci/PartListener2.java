package abapci;

import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;
import abapci.coloredProject.general.EditorActivationHandler;
import abapci.presenter.GeneralThemePresenter;

public class PartListener2 implements IPartListener2 {


	public PartListener2(GeneralThemePresenter generalThemePresenter) {
	}

	@Override
	public void partActivated(IWorkbenchPartReference arg0) {
		try {
			EditorActivationHandler editorActivationHandler = new EditorActivationHandler();
			editorActivationHandler.updateDisplayColoring();
		} catch (Exception ex) {
			// if there happens any error while formatting the editor, we skip it as this
			// function is not mandatory
			// but at leas write the trace
			ex.printStackTrace();
		}
	}

	@Override
	public void partBroughtToTop(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void partClosed(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void partDeactivated(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void partHidden(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void partInputChanged(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void partOpened(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void partVisible(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub
	}

}
