import React from "react";
import { useNavigate } from "react-router-dom";

function SubmitButton() {
  const navigate = useNavigate();

  const handleSubmit = () => {
    navigate("/searchResult");
  };

  return (
    <button onClick={handleSubmit} type="button" class="btn btn-primary">
      <i class="fas fa-search"></i>
    </button>
  );

}

export default SubmitButton;