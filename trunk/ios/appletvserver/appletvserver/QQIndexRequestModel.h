//
//  QQIndexRequestModel.h
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//
#import "Constants.h"

@interface QQIndexRequestModel : TTURLRequestModel{
        NSString* _keyword;
        QQChannel _qqChannel;
        int _page;             
        int _resultsPerPage;  
        NSMutableArray* _videoList;
        BOOL _finished;
}

@property (nonatomic, assign) QQChannel qqChannel;
@property (nonatomic, copy)     NSString*       keyword;
@property (nonatomic, retain) NSMutableArray* videoList;
@property (nonatomic, readonly) BOOL            finished;
@end
