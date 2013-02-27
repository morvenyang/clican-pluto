//
//  HTTPDataHeaderResponse.h
//  appletvserver
//
//  Created by zhang wei on 13-2-27.
//
//

#import <Foundation/Foundation.h>
#import "HTTPResponse.h"


@interface HTTPDataHeaderResponse : NSObject <HTTPResponse>
{
	NSUInteger offset;
	NSData *data;
    NSMutableDictionary* headers;
}

- (id)initWithData:(NSData *)data;

@end
