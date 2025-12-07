// import logo from './logo.svg';
// import './App.css';
import {BrowserRouter,Routes,Route} from "react-router-dom";
import MainPortal from './main_portal'
function App() {
  return(
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<MainPortal />}/>
        </Routes>
      </BrowserRouter>
  )
}

export default App;
