package org.powertac.samplebroker.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.10.0)",
    comments = "Source: grpc.proto")
public final class ControlEventHandlerGrpc {

  private ControlEventHandlerGrpc() {}

  public static final String SERVICE_NAME = "ControlEventHandler";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getSubmitControlEventMethod()} instead. 
  public static final io.grpc.MethodDescriptor<org.powertac.samplebroker.grpc.ControlEvent,
      org.powertac.samplebroker.grpc.Booly> METHOD_SUBMIT_CONTROL_EVENT = getSubmitControlEventMethodHelper();

  private static volatile io.grpc.MethodDescriptor<org.powertac.samplebroker.grpc.ControlEvent,
      org.powertac.samplebroker.grpc.Booly> getSubmitControlEventMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<org.powertac.samplebroker.grpc.ControlEvent,
      org.powertac.samplebroker.grpc.Booly> getSubmitControlEventMethod() {
    return getSubmitControlEventMethodHelper();
  }

  private static io.grpc.MethodDescriptor<org.powertac.samplebroker.grpc.ControlEvent,
      org.powertac.samplebroker.grpc.Booly> getSubmitControlEventMethodHelper() {
    io.grpc.MethodDescriptor<org.powertac.samplebroker.grpc.ControlEvent, org.powertac.samplebroker.grpc.Booly> getSubmitControlEventMethod;
    if ((getSubmitControlEventMethod = ControlEventHandlerGrpc.getSubmitControlEventMethod) == null) {
      synchronized (ControlEventHandlerGrpc.class) {
        if ((getSubmitControlEventMethod = ControlEventHandlerGrpc.getSubmitControlEventMethod) == null) {
          ControlEventHandlerGrpc.getSubmitControlEventMethod = getSubmitControlEventMethod = 
              io.grpc.MethodDescriptor.<org.powertac.samplebroker.grpc.ControlEvent, org.powertac.samplebroker.grpc.Booly>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "ControlEventHandler", "submitControlEvent"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.powertac.samplebroker.grpc.ControlEvent.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.powertac.samplebroker.grpc.Booly.getDefaultInstance()))
                  .setSchemaDescriptor(new ControlEventHandlerMethodDescriptorSupplier("submitControlEvent"))
                  .build();
          }
        }
     }
     return getSubmitControlEventMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ControlEventHandlerStub newStub(io.grpc.Channel channel) {
    return new ControlEventHandlerStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ControlEventHandlerBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new ControlEventHandlerBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ControlEventHandlerFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new ControlEventHandlerFutureStub(channel);
  }

  /**
   */
  public static abstract class ControlEventHandlerImplBase implements io.grpc.BindableService {

    /**
     */
    public void submitControlEvent(org.powertac.samplebroker.grpc.ControlEvent request,
        io.grpc.stub.StreamObserver<org.powertac.samplebroker.grpc.Booly> responseObserver) {
      asyncUnimplementedUnaryCall(getSubmitControlEventMethodHelper(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSubmitControlEventMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                org.powertac.samplebroker.grpc.ControlEvent,
                org.powertac.samplebroker.grpc.Booly>(
                  this, METHODID_SUBMIT_CONTROL_EVENT)))
          .build();
    }
  }

  /**
   */
  public static final class ControlEventHandlerStub extends io.grpc.stub.AbstractStub<ControlEventHandlerStub> {
    private ControlEventHandlerStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ControlEventHandlerStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ControlEventHandlerStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ControlEventHandlerStub(channel, callOptions);
    }

    /**
     */
    public void submitControlEvent(org.powertac.samplebroker.grpc.ControlEvent request,
        io.grpc.stub.StreamObserver<org.powertac.samplebroker.grpc.Booly> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSubmitControlEventMethodHelper(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class ControlEventHandlerBlockingStub extends io.grpc.stub.AbstractStub<ControlEventHandlerBlockingStub> {
    private ControlEventHandlerBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ControlEventHandlerBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ControlEventHandlerBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ControlEventHandlerBlockingStub(channel, callOptions);
    }

    /**
     */
    public org.powertac.samplebroker.grpc.Booly submitControlEvent(org.powertac.samplebroker.grpc.ControlEvent request) {
      return blockingUnaryCall(
          getChannel(), getSubmitControlEventMethodHelper(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class ControlEventHandlerFutureStub extends io.grpc.stub.AbstractStub<ControlEventHandlerFutureStub> {
    private ControlEventHandlerFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ControlEventHandlerFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ControlEventHandlerFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ControlEventHandlerFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.powertac.samplebroker.grpc.Booly> submitControlEvent(
        org.powertac.samplebroker.grpc.ControlEvent request) {
      return futureUnaryCall(
          getChannel().newCall(getSubmitControlEventMethodHelper(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SUBMIT_CONTROL_EVENT = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ControlEventHandlerImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ControlEventHandlerImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SUBMIT_CONTROL_EVENT:
          serviceImpl.submitControlEvent((org.powertac.samplebroker.grpc.ControlEvent) request,
              (io.grpc.stub.StreamObserver<org.powertac.samplebroker.grpc.Booly>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class ControlEventHandlerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ControlEventHandlerBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.powertac.samplebroker.grpc.Grpc.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ControlEventHandler");
    }
  }

  private static final class ControlEventHandlerFileDescriptorSupplier
      extends ControlEventHandlerBaseDescriptorSupplier {
    ControlEventHandlerFileDescriptorSupplier() {}
  }

  private static final class ControlEventHandlerMethodDescriptorSupplier
      extends ControlEventHandlerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ControlEventHandlerMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ControlEventHandlerGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ControlEventHandlerFileDescriptorSupplier())
              .addMethod(getSubmitControlEventMethodHelper())
              .build();
        }
      }
    }
    return result;
  }
}
