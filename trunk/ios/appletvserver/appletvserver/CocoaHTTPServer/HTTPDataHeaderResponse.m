#import "HTTPDataHeaderResponse.h"
#import "HTTPLogging.h"

#if ! __has_feature(objc_arc)
#warning This file must be compiled with ARC. Use -fobjc-arc flag (or convert project to ARC).
#endif

// Log levels : off, error, warn, info, verbose
// Other flags: trace
static const int httpLogLevel = HTTP_LOG_LEVEL_OFF; // | HTTP_LOG_FLAG_TRACE;


@implementation HTTPDataHeaderResponse

- (id)initWithData:(NSData *)dataParam
{
	if((self = [super init]))
	{
		HTTPLogTrace();
		
		offset = 0;
		data = dataParam;
        headers = [NSMutableDictionary dictionary];
	}
	return self;
}

- (id)initWithData:(NSData *)dataParam status:(NSInteger)statusParam
{
	if((self = [super init]))
	{
		HTTPLogTrace();
		
		offset = 0;
		data = dataParam;
        headers = [NSMutableDictionary dictionary];
        status = statusParam;
	}
	return self;
}

- (id)initWithData:(NSData *)dataParam status:(NSInteger)statusParam length:(UInt64)lengthParam{
    if((self = [super init]))
	{
		HTTPLogTrace();
		
		offset = 0;
		data = dataParam;
        headers = [NSMutableDictionary dictionary];
        status = statusParam;
        length = lengthParam;
	}
	return self;
}

- (void)dealloc
{
	HTTPLogTrace();
	
}

- (UInt64)contentLength
{
    if(length>0){
        return length;
    }
	UInt64 result = (UInt64)[data length];
	
	HTTPLogTrace2(@"%@[%p]: contentLength - %llu", THIS_FILE, self, result);
	
	return result;
}

- (UInt64)offset
{
	HTTPLogTrace();
	
	return offset;
}

- (void)setOffset:(UInt64)offsetParam
{
	HTTPLogTrace2(@"%@[%p]: setOffset:%lu", THIS_FILE, self, (unsigned long)offset);
	
	offset = (NSUInteger)offsetParam;
}

- (NSData *)readDataOfLength:(NSUInteger)lengthParameter
{
	HTTPLogTrace2(@"%@[%p]: readDataOfLength:%lu", THIS_FILE, self, (unsigned long)lengthParameter);
	
	NSUInteger remaining = [data length] - offset;
	NSUInteger length = lengthParameter < remaining ? lengthParameter : remaining;
	
	void *bytes = (void *)([data bytes] + offset);
	
	offset += length;
	
	return [NSData dataWithBytesNoCopy:bytes length:length freeWhenDone:NO];
}

- (BOOL)isDone
{
	BOOL result = (offset == [data length]);
	
	HTTPLogTrace2(@"%@[%p]: isDone - %@", THIS_FILE, self, (result ? @"YES" : @"NO"));
	
	return result;
}

- (NSDictionary *)httpHeaders{
    return headers;
}

- (NSInteger) status{
    return status;
}
@end
