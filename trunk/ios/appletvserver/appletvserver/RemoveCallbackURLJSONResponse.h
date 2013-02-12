//
//  RemoveCallbackURLJSONResponse.h
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface RemoveCallbackURLJSONResponse : NSObject<TTURLResponse>{
    id _rootObject;
    NSString* callback;
}

@property (nonatomic, retain, readonly) id    rootObject;
@property (nonatomic, copy)     NSString*       callback;

- (id)initCallback:(NSString*)callback;
@end
