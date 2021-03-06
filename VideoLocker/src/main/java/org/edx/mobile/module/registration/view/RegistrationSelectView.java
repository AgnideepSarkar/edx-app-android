package org.edx.mobile.module.registration.view;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import org.edx.mobile.R;
import org.edx.mobile.logger.Logger;
import org.edx.mobile.module.registration.model.RegistrationFormField;
import org.edx.mobile.module.registration.model.RegistrationOption;

class RegistrationSelectView implements IRegistrationFieldView {

    protected static final Logger logger = new Logger(RegistrationEditTextView.class);
    private RegistrationFormField mField;
    private View mView;
    protected RegistrationOptionSpinner mInputView;
    private TextView mErrorView, mInstructionView;

    public RegistrationSelectView(RegistrationFormField field, View view) {
        // create and configure view and save it to an instance variable
        this.mField = field;
        this.mView = view;

        this.mInputView = (RegistrationOptionSpinner) view.findViewById(R.id.input_spinner);
        this.mErrorView = (TextView) view.findViewById(R.id.input_spinner_error);
        this.mInstructionView = (TextView) view.findViewById(R.id.input_spinner_instruction);

        // set prompt
        mInputView.setPrompt(mField.getLabel());

        // set hint
        mInputView.setHint(mField.getLabel());

        RegistrationOption defaultOption = null;
        for (RegistrationOption option : mField.getOptions()) {
            if (option.isDefaultValue()) {
                defaultOption = option;
                break;
            }
        }
        mInputView.setItems(mField.getOptions(),defaultOption);

        // display instructions if available
        if (mField.getInstructions() != null && !mField.getInstructions().isEmpty()) {
            mInstructionView.setVisibility(View.VISIBLE);
            mInstructionView.setText(mField.getInstructions());
        } else {
            mInstructionView.setVisibility(View.GONE);
        }

        // hide error text view
        mErrorView.setVisibility(View.GONE);
    }

    @Override
    public JsonElement getCurrentValue() {
        // turn text view content into a JsonElement and return it
        return new JsonPrimitive(mInputView.getSelectedItem().getValue());
    }

    @Override
    public boolean hasValue() {
        return (mInputView.getSelectedItem() != null
                && !TextUtils.isEmpty(mInputView.getSelectedItem().getValue()));
    }

    @Override
    public RegistrationFormField getField() {
        return mField;
    }

    @Override
    public View getView() {
        return mView;
    }

    @Override
    public void handleError(String error) {
        if (error != null && !error.isEmpty()) {
            mErrorView.setVisibility(View.VISIBLE);
            mErrorView.setText(error);
        }
        else {
            logger.warn("error message not provided, so not informing the user about this error");
        }
    }

    @Override
    public boolean isValidInput() {
        // hide error as we are re-validating the input
        mErrorView.setVisibility(View.GONE);

        // check if this is required field and has an input value
        if (mField.isRequired() && !hasValue()) {
            String errorMessage = mField.getErrorMessage().getRequired();
            if(errorMessage==null || errorMessage.isEmpty()){
                errorMessage = getView().getResources().getString(R.string.error_select_field,
                        mField.getLabel());
            }
            handleError(errorMessage);
            return false;
        }

        //For select we should not have length checks as there is no input

        return true;
    }

    @Override
    public void setEnabled(boolean enabled) {
        mInputView.setEnabled(enabled);
    }

    @Override
    public void setActionListener(IActionListener actionListener) {
        // no actions for this field
    }
}
