//
//  QQIndexDataSource.h
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//
#import "QQIndexRequestModel.h"

@interface QQIndexDataSource : TTListDataSource{
    QQIndexRequestModel* _qqIndexRequestModel;
}

@property (nonatomic,retain) QQIndexRequestModel* qqIndexRequestModel;

- (id)initWithQQChannel:(QQChannel)channel;

@end
