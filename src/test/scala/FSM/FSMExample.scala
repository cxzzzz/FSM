package libpc.FSM

import chisel3._

class CFExample extends Module {
  val io = IO(new Bundle{
    val in = Input(Bool())
    val output = Output(Bool())
  })

  val fsm = new ControlFlow {
    start {
      io.output := false.B
    }

    tik {
      io.output := true.B
    }.tag("tag1")

    run {
      tik {
        io.output := false.B
      }
      tik {
        io.output := true.B
      }
    }.until(io.in)

    loop(!io.in) {
      tik {
        io.output := false.B
      }
    }

    repeat(3) {
      tik {
        io.output := true.B
      }
    }

    branch(io.in) {
      tik {

      }
    }.or_branch(!io.in) {
      tik {

      }
    }.or {
      goto("tag1")
    }

    subFSM(new FSM {
      entryState("subStart").otherwise.transferToEnd
    })

  }
}

class FSMExample extends Module {
  val io = IO(new Bundle {
    val input = Input(new Bundle {
      val w_i = Bool()
    })
    val output = Output(new Bundle {
      val z_o = Bool()
    })
  })

  io.output.z_o := false.B

  val fsm = InstanciateFSM(new FSM {
    entryState("Idle")
      .act {
        io.output.z_o := false.B
      }
      .when(io.input.w_i === true.B).transferTo("s0")

    state("s0")
      .act {
        io.output.z_o := false.B
      }
      .when(io.input.w_i === false.B).transferTo("Idle")
      .when(io.input.w_i === true.B).transferTo("s1")

    state("s1")
      .act {
        io.output.z_o := true.B
      }
      .when(!io.input.w_i).transferTo("Idle")
      .otherwise.transferTo("s1")
  })
}

object FSMMain extends App {
  chisel3.Driver.execute(args, () => new FSMExample)
}
