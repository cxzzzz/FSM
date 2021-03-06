package fsm.core

abstract class FSMException extends Exception {}

class MultipleEntryException extends FSMException {}

abstract class FSMCompileException extends FSMException {}

class FSMCompileNoStopping extends FSMCompileException {}

class FSMCompileEdgeNotFound extends FSMCompileException {}
class FSMCompileStateNotFound extends FSMCompileException {}
