// App.tsx

import React, { useMemo } from 'react';
import { Provider, useSelector } from 'react-redux';
import { store, RootState } from './Store';
import { Navigate, BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import {
  faHome,
  faPhone,
  faEnvelope,
  faCube,
  faCalendar,
  faUser,
  faCartShopping,
  faShop,
  faUserPlus,
  faSun,
  faPowerOff,
  faClock
} from '@fortawesome/free-solid-svg-icons';

import { HexLink } from './Components';
import { isSessionAuthenticated } from './Utils';
import './App.css';

import {
  PATHS,
  HomePage,
  LoginPage,
  SignUpPage,
  ResetPage,
  ProductsPage,
  UsersPage,
  SalesPage,
  PurchasesPage,
  MessagesPage,
  MeetingsPage,
  WorkPage,
  LogoutPage,
  SchedulePage,
  PtoPage,
  UserDetailsPage,
  ProductDetailsPage,
  SaleDetailsPage,
  PurchaseDetailsPage,
  MeetingDetailsPage,
  MessageDetailsPage,
  WorkDetailsPage,
  PtoDetailsPage,
  ScheduleDetailsPage,
  UserEditPage,
  ProductCreatePage,
  ProductEditPage,
  SaleCreatePage,
  PurchaseCreatePage,
  MeetingCreatePage,
  MessageCreatePage,
  ScheduleCreatePage,
  PtoCreatePage,
  AddRecipientPage,
  MeetingInvitePage,
  MeetingAttachPage,
} from './Pages';

export const HexNav = () => {
  const session = useSelector((state: RootState) => state.session);
  const isAuthenticated = useMemo(() => isSessionAuthenticated(session), [session.id]);
  const Element = ({ children }: { children: any }) => {
      return <div className="nav-item p-1 text-blue-500 hover:text-blue-700">
        { children}
      </div>
  }
  return <nav className="bg-white border-gray-300 p-2 justify-center">
    <div className='navbar justify-center'>
      {
        isAuthenticated ? <>
          <Element><HexLink path={PATHS.HOME} icon={faHome} /></Element>
          <Element><HexLink path={PATHS.USERS} icon={faUser} /></Element>
          <Element><HexLink path={PATHS.SCHEDULE} icon={faCalendar} /></Element>
          <Element><HexLink path={PATHS.PTO} icon={faSun} /></Element>
          <Element><HexLink path={PATHS.WORK} icon={faClock} /></Element>
          <Element><HexLink path={PATHS.MESSAGES} icon={faEnvelope} /></Element>
          <Element><HexLink path={PATHS.MEETINGS} icon={faPhone} /></Element>
          <Element><HexLink path={PATHS.PRODUCTS} icon={faCube} /></Element>
          <Element><HexLink path={PATHS.PURCHASES} icon={faCartShopping} /></Element>
          <Element><HexLink path={PATHS.SALES} icon={faShop} /></Element>
          <Element><HexLink path={PATHS.LOGOUT} icon={faPowerOff} /></Element>
        </> : <>
          <Element><HexLink path={PATHS.LOGIN} icon={faUser} text="Login" /></Element>
          <Element><HexLink path={PATHS.SIGNUP} icon={faUserPlus} text="Sign Up" /></Element>
          <Element><HexLink path={PATHS.RESET} icon={faEnvelope} text="Recover Password" /></Element>
        </>
      }
    </div>
  </nav>
}

function HexLayout() {
  const session = useSelector((state: RootState) => state.session);
  const isAuthenticated = useMemo(() => isSessionAuthenticated(session), [session]);
  return (
    <div className="App bg-gray-100 min-h-screen flex flex-col justify-center">
      <HexNav />
      <div className="container max-w-4xl mx-auto p-4">
        <Routes>
          {
            !isAuthenticated ? <>
              <Route path={PATHS.SIGNUP} element={!isAuthenticated ? <SignUpPage /> : <Navigate to={PATHS.HOME} />} />
              <Route path={PATHS.RESET} element={!isAuthenticated ? <ResetPage /> : <Navigate to={PATHS.HOME} />} />
              <Route path={'*'} element={!isAuthenticated ? <LoginPage /> : <Navigate to={PATHS.HOME} />} />
            </> : <>
              <Route path={PATHS.ADD_RECIPIENT} element={<AddRecipientPage/>}/>
              <Route path={PATHS.ATTACH} element={<MeetingAttachPage/>}/>
              <Route path={PATHS.INVITE} element={<MeetingInvitePage/>}/>
              <Route path={PATHS.USER_EDIT} element={<UserEditPage/>}/>
              <Route path={PATHS.PRODUCT_CREATE} element={<ProductCreatePage/>}/>
              <Route path={PATHS.PRODUCT_EDIT} element={<ProductEditPage/>}/>
              <Route path={PATHS.SALE_CREATE} element={<SaleCreatePage/>}/>
              <Route path={PATHS.PURCHASE_CREATE} element={<PurchaseCreatePage/>}/>
              <Route path={PATHS.MEETING_CREATE} element={<MeetingCreatePage/>}/>
              <Route path={PATHS.MESSAGE_CREATE} element={<MessageCreatePage/>}/>
              <Route path={PATHS.SCHEDULE_CREATE} element={<ScheduleCreatePage/>}/>
              <Route path={PATHS.PTO_CREATE} element={<PtoCreatePage/>}/>
              <Route path={PATHS.PRODUCTS} element={<ProductsPage/>}/>
              <Route path={PATHS.USERS} element={<UsersPage/>}/>
              <Route path={PATHS.SALES} element={<SalesPage/>}/>
              <Route path={PATHS.PURCHASES} element={<PurchasesPage/>}/>
              <Route path={PATHS.MESSAGES} element={<MessagesPage/>}/>
              <Route path={PATHS.MEETINGS} element={<MeetingsPage/>}/>
              <Route path={PATHS.PTO} element={<PtoPage/>}/>
              <Route path={PATHS.SCHEDULE} element={<SchedulePage/>}/>
              <Route path={PATHS.WORK} element={<WorkPage/>}/>
              <Route path={PATHS.LOGOUT} element={<LogoutPage/>}/>
              <Route path={PATHS.USER_DETAILS} element={<UserDetailsPage/>}/>
              <Route path={PATHS.PRODUCT_DETAILS} element={<ProductDetailsPage/>}/>
              <Route path={PATHS.SALE_DETAILS} element={<SaleDetailsPage/>}/>
              <Route path={PATHS.PURCHASE_DETAILS} element={<PurchaseDetailsPage/>}/>
              <Route path={PATHS.MESSAGE_DETAILS} element={<MessageDetailsPage/>}/>
              <Route path={PATHS.MEETING_DETAILS} element={<MeetingDetailsPage/>}/>
              <Route path={PATHS.WORK_DETAILS} element={<WorkDetailsPage/>}/>
              <Route path={PATHS.PTO_DETAILS} element={<PtoDetailsPage/>}/>
              <Route path={PATHS.SCHEDULE_DETAILS} element={<ScheduleDetailsPage/>}/>
              <Route path={'*'} element={<HomePage/>}/>
            </>
          }
        </Routes>
      </div>
    </div>
  );
}

function App() {
  return (
    <Provider store={store}>
      <Router>
        <HexLayout/>
      </Router>
      <ToastContainer
        position="bottom-right"
        autoClose={5000}
        hideProgressBar={true}
        newestOnTop={false}
        closeOnClick={true}
        rtl={false}
        pauseOnFocusLoss={false}
        draggable={false}
        pauseOnHover={false}
        theme="light"
      />
    </Provider>
  );
}

export default App;