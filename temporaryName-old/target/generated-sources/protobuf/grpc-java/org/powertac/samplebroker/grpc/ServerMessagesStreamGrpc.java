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
public final class ServerMessagesStreamGrpc {

  private ServerMessagesStreamGrpc() {}

  public static final String SERVICE_NAME = "ServerMessagesStream";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getRegisterListenerMethod()} instead. 
  public static final io.grpc.MethodDescriptor<org.powertac.samplebroker.grpc.Booly,
      org.powertac.samplebroker.grpc.XmlMessage> METHOD_REGISTER_LISTENER = getRegisterListenerMethodHelper();

  private static volatile io.grpc.MethodDescriptor<org.powertac.samplebroker.grpc.Booly,
      org.powertac.samplebroker.grpc.XmlMessage> getRegisterListenerMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<org.powertac.samplebroker.grpc.Booly,
      org.powertac.samplebroker.grpc.XmlMessage> getRegisterListenerMethod() {
    return getRegisterListenerMethodHelper();
  }

  private static io.grpc.MethodDescriptor<org.powertac.samplebroker.grpc.Booly,
      org.powertac.samplebroker.grpc.XmlMessage> getRegisterListenerMethodHelper() {
    io.grpc.MethodDescriptor<org.powertac.samplebroker.grpc.Booly, org.powertac.samplebroker.grpc.XmlMessage> getRegisterListenerMethod;
    if ((getRegisterListenerMethod = ServerMessagesStreamGrpc.getRegisterListenerMethod) == null) {
      synchronized (ServerMessagesStreamGrpc.class) {
        if ((getRegisterListenerMethod = ServerMessagesStreamGrpc.getRegisterListenerMethod) == null) {
          ServerMessagesStreamGrpc.getRegisterListenerMethod = getRegisterListenerMethod = 
              io.grpc.MethodDescriptor.<org.powertac.samplebroker.grpc.Booly, org.powertac.samplebroker.grpc.XmlMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "ServerMessagesStream", "registerListener"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.powertac.samplebroker.grpc.Booly.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.powertac.samplebroker.grpc.XmlMessage.getDefaultInstance()))
                  .setSchemaDescriptor(new ServerMessagesStreamMethodDescriptorSupplier("registerListener"))
                  .build();
          }
        }
     }
     return getRegisterListenerMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getRegisterEventSourceMethod()} instead. 
  public static final io.grpc.MethodDescriptor<org.powertac.samplebroker.grpc.XmlMessage,
      org.powertac.samplebroker.grpc.Booly> METHOD_REGISTER_EVENT_SOURCE = getRegisterEventSourceMethodHelper();

  private static volatile io.grpc.MethodDescriptor<org.powertac.samplebroker.grpc.XmlMessage,
      org.powertac.samplebroker.grpc.Booly> getRegisterEventSourceMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<org.powertac.samplebroker.grpc.XmlMessage,
      org.powertac.samplebroker.grpc.Booly> getRegisterEventSourceMethod() {
    return getRegisterEventSourceMethodHelper();
  }

  private static io.grpc.MethodDescriptor<org.powertac.samplebroker.grpc.XmlMessage,
      org.powertac.samplebroker.grpc.Booly> getRegisterEventSourceMethodHelper() {
    io.grpc.MethodDescriptor<org.powertac.samplebroker.grpc.XmlMessage, org.powertac.samplebroker.grpc.Booly> getRegisterEventSourceMethod;
    if ((getRegisterEventSourceMethod = ServerMessagesStreamGrpc.getRegisterEventSourceMethod) == null) {
      synchronized (ServerMessagesStreamGrpc.class) {
        if ((getRegisterEventSourceMethod = ServerMessagesStreamGrpc.getRegisterEventSourceMethod) == null) {
          ServerMessagesStreamGrpc.getRegisterEventSourceMethod = getRegisterEventSourceMethod = 
              io.grpc.MethodDescriptor.<org.powertac.samplebroker.grpc.XmlMessage, org.powertac.samplebroker.grpc.Booly>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "ServerMessagesStream", "registerEventSource"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.powertac.samplebroker.grpc.XmlMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.powertac.samplebroker.grpc.Booly.getDefaultInstance()))
                  .setSchemaDescriptor(new ServerMessagesStreamMethodDescriptorSupplier("registerEventSource"))
                  .build();
          }
        }
     }
     return getRegisterEventSourceMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ServerMessagesStreamStub newStub(io.grpc.Channel channel) {
    return new ServerMessagesStreamStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ServerMessagesStreamBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new ServerMessagesStreamBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ServerMessagesStreamFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new ServerMessagesStreamFutureStub(channel);
  }

  /**
   */
  public static abstract class ServerMessagesStreamImplBase implements io.grpc.BindableService {

    /**
     */
    public void registerListener(org.powertac.samplebroker.grpc.Booly request,
        io.grpc.stub.StreamObserver<org.powertac.samplebroker.grpc.XmlMessage> responseObserver) {
      asyncUnimplementedUnaryCall(getRegisterListenerMethodHelper(), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<org.powertac.samplebroker.grpc.XmlMessage> registerEventSource(
        io.grpc.stub.StreamObserver<org.powertac.samplebroker.grpc.Booly> responseObserver) {
      return asyncUnimplementedStreamingCall(getRegisterEventSourceMethodHelper(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getRegisterListenerMethodHelper(),
            asyncServerStreamingCall(
              new MethodHandlers<
                org.powertac.samplebroker.grpc.Booly,
                org.powertac.samplebroker.grpc.XmlMessage>(
                  this, METHODID_REGISTER_LISTENER)))
          .addMethod(
            getRegisterEventSourceMethodHelper(),
            asyncClientStreamingCall(
              new MethodHandlers<
                org.powertac.samplebroker.grpc.XmlMessage,
                org.powertac.samplebroker.grpc.Booly>(
                  this, METHODID_REGISTER_EVENT_SOURCE)))
          .build();
    }
  }

  /**
   */
  public static final class ServerMessagesStreamStub extends io.grpc.stub.AbstractStub<ServerMessagesStreamStub> {
    private ServerMessagesStreamStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ServerMessagesStreamStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ServerMessagesStreamStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ServerMessagesStreamStub(channel, callOptions);
    }

    /**
     */
    public void registerListener(org.powertac.samplebroker.grpc.Booly request,
        io.grpc.stub.StreamObserver<org.powertac.samplebroker.grpc.XmlMessage> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getRegisterListenerMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<org.powertac.samplebroker.grpc.XmlMessage> registerEventSource(
        io.grpc.stub.StreamObserver<org.powertac.samplebroker.grpc.Booly> responseObserver) {
      return asyncClientStreamingCall(
          getChannel().newCall(getRegisterEventSourceMethodHelper(), getCallOptions()), responseObserver);
    }
  }

  /**
   */
  public static final class ServerMessagesStreamBlockingStub extends io.grpc.stub.AbstractStub<ServerMessagesStreamBlockingStub> {
    private ServerMessagesStreamBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ServerMessagesStreamBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ServerMessagesStreamBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ServerMessagesStreamBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<org.powertac.samplebroker.grpc.XmlMessage> registerListener(
        org.powertac.samplebroker.grpc.Booly request) {
      return blockingServerStreamingCall(
          getChannel(), getRegisterListenerMethodHelper(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class ServerMessagesStreamFutureStub extends io.grpc.stub.AbstractStub<ServerMessagesStreamFutureStub> {
    private ServerMessagesStreamFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ServerMessagesStreamFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ServerMessagesStreamFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ServerMessagesStreamFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_REGISTER_LISTENER = 0;
  private static final int METHODID_REGISTER_EVENT_SOURCE = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ServerMessagesStreamImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ServerMessagesStreamImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_REGISTER_LISTENER:
          serviceImpl.registerListener((org.powertac.samplebroker.grpc.Booly) request,
              (io.grpc.stub.StreamObserver<org.powertac.samplebroker.grpc.XmlMessage>) responseObserver);
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
        case METHODID_REGISTER_EVENT_SOURCE:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.registerEventSource(
              (io.grpc.stub.StreamObserver<org.powertac.samplebroker.grpc.Booly>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class ServerMessagesStreamBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ServerMessagesStreamBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.powertac.samplebroker.grpc.Grpc.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ServerMessagesStream");
    }
  }

  private static final class ServerMessagesStreamFileDescriptorSupplier
      extends ServerMessagesStreamBaseDescriptorSupplier {
    ServerMessagesStreamFileDescriptorSupplier() {}
  }

  private static final class ServerMessagesStreamMethodDescriptorSupplier
      extends ServerMessagesStreamBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ServerMessagesStreamMethodDescriptorSupplier(String methodName) {
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
      synchronized (ServerMessagesStreamGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ServerMessagesStreamFileDescriptorSupplier())
              .addMethod(getRegisterListenerMethodHelper())
              .addMethod(getRegisterEventSourceMethodHelper())
              .build();
        }
      }
    }
    return result;
  }
}
