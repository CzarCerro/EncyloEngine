import React from 'react';
import './styles/SwitchButton.css';

const SwitchButton = ({isToggled, handleToggle}) => {
  return (
    <>
      <input
        className="react-switch-checkbox"
        id={`react-switch-new`}
        type="checkbox"
        onChange={handleToggle}
      />
        <label
        style={{ background: isToggled && '#06D6A0' }}
        className="react-switch-label"
        htmlFor={`react-switch-new`}
        >
        <span className={`react-switch-button`} />
      </label>
    </>
  );
};

export default SwitchButton;