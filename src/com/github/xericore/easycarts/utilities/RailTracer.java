package com.github.xericore.easycarts.utilities;

import com.github.xericore.easycarts.data.RailTrace;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Rail;

import java.util.ArrayList;
import java.util.List;

public class RailTracer
{
    private List<Rail.Shape> _tracedRailShapes = new ArrayList<>();
    private int _traceLength;

    public List<Rail.Shape> traceRails(Block block, BlockFace facing, int traceLength)
    {
        _traceLength = traceLength;

        traceRailRecursive(block, facing);

        return _tracedRailShapes;
    }

    private void traceRailRecursive(Block block, BlockFace facing)
    {
        _traceLength--;

        while (_traceLength > 0)
        {
            RailTrace nextTrace = new RailTrace(block, facing);

            _tracedRailShapes.add(nextTrace.getNextRailShape());

            traceRailRecursive(nextTrace.getNextBlock(), nextTrace.getNextFacing());
        }
    }
}
