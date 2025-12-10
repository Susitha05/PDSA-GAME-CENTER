// import logo from './logo.svg';
// import './App.css';
import {BrowserRouter,Routes,Route} from "react-router-dom";
import MainPortal from './main_portal';
import SnakeLadder from './SnakeLadder';

function App() {
  return(
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<MainPortal />}/>
          <Route path="/snake-ladder" element={<SnakeLadder />}/>
        </Routes>
      </BrowserRouter>
  )
}

export default App;
